using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using CsvHelper;
using CsvHelper.Configuration;
using DaleNewman;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using Npgsql;
using NpgsqlTypes;
using PostgreSQLCopyHelper;
using webapi.Models;

namespace webapi.Controllers;

[ApiController]
[Route("[controller]")]
public class ConditionsController : ControllerBase
{
    private readonly WeatherContext _context;
    private readonly ILogger<ConditionsController> _logger;

    public ConditionsController(WeatherContext context, ILogger<ConditionsController> logger)
    {
        _context = context ?? throw new ArgumentNullException(nameof(context));
        _logger = logger ?? throw new ArgumentNullException(nameof(logger));
    }

    [HttpGet]
    public IEnumerable<Condition> Get(string from = "now-15m", string to = "now", int size = 10, int page = 0, string deviceId=null)
    {
        try
        {
            if (!DateMath.TryParse(from, out var fromTime))
                throw new ArgumentException("Could not parse date math expression", nameof(from));
            if (!DateMath.TryParse(to, out var toTime))
                throw new ArgumentException("Could not parse date math expression", nameof(to));
            return _context.Conditions
                .Where(c => fromTime < c.Time && c.Time <= toTime)
                .Where(c => string.IsNullOrWhiteSpace(deviceId) || c.DeviceId == deviceId)
                .OrderByDescending(c => c.Time)
                .Skip(page * size)
                .Take(size);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Could not get conditions");
            throw;
        }
    }

    [HttpPost, Route("upload")]
    [DisableRequestSizeLimit]
    public async Task<IActionResult> UploadAsync()
    {
        // cf.: https://weblog.west-wind.com/posts/2017/sep/14/accepting-raw-request-body-content-in-aspnet-core-api-controllers
        using var reader = new StreamReader(Request.Body);
        var content = await reader.ReadToEndAsync();
        _logger.LogDebug("Read request body");

        using var textReader = new StringReader(content);
        var config = new CsvConfiguration(CultureInfo.InvariantCulture)
        {
            HasHeaderRecord = false,
            Delimiter = ",",
            MissingFieldFound = null // ignore missing fields
        };
        using var csv = new CsvReader(textReader, config);
        var conditions = csv.GetRecords<Condition>();
        _logger.LogDebug("Parsed CSV records");


        // bulk insert for Npgsql, cf.: https://github.com/PostgreSQLCopyHelper/PostgreSQLCopyHelper
        var copyHelper = new PostgreSQLCopyHelper<Condition>("conditions")
                .MapTimeStampTz("time", x => x.Time)
                .MapCharacter("device_id", x => x.DeviceId)
                .MapDouble("temperature", x => ToCelsius(x.Temperature))
                .MapDouble("humidity", x => x.Humidity)
                .MapNullable("location", x => PointOrRandomLatLong(x.Location), NpgsqlDbType.Point)
            ;
        await using var connection = new NpgsqlConnection(_context.Database.GetDbConnection().ConnectionString);
        connection.Open();
        var count = await copyHelper.SaveAllAsync(connection, conditions);
        _logger.LogDebug("Bulk-inserted ${Count} records", count);

        return Ok(count);
    }

    private static double? ToCelsius(double? fahrenheit)
    {
        return (fahrenheit - 32.0) /1.8;
    }

    private static NpgsqlPoint? PointOrRandomLatLong(NpgsqlPoint? point)
    {
        if (point.HasValue) return point.Value;
        var lon = Rnd.NextDouble() * 360.0 - 180.0;
        var lat = Rnd.NextDouble() * 180.0 - 90.0;
        return new NpgsqlPoint(lon, lat);
    }
    private static readonly  Random Rnd = new Random();
}
