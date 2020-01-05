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
using PostgreSQLCopyHelper;
using webapi.Models;

namespace webapi.Controllers
{
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
        public IEnumerable<Condition> Get(string from = "now-15m", string to = "now", int size = 10, int page = 0)
        {
            try
            {
                if (!DateMath.TryParse(from, out var fromTime))
                    throw new ArgumentException("Could not parse date math expression", nameof(from));
                if (!DateMath.TryParse(to, out var toTime))
                    throw new ArgumentException("Could not parse date math expression", nameof(to));
                return _context.Conditions
                    .Where(c => fromTime < c.Time && c.Time <= toTime)
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
        public async Task<IActionResult> Upload()
        {
            _logger.LogInformation("Reading request body...");
            // cf.: https://weblog.west-wind.com/posts/2017/sep/14/accepting-raw-request-body-content-in-aspnet-core-api-controllers
            using var reader = new StreamReader(Request.Body);
            var content = await reader.ReadToEndAsync();
            using var textReader = new StringReader(content);
            var config = new Configuration(CultureInfo.InvariantCulture)
            {
                HasHeaderRecord = false,
                Delimiter = ","
            };
            using var csv = new CsvReader(textReader, config);
            _logger.LogInformation("Parsing CSV records...");
            var conditions = csv.GetRecords<Condition>();


            _logger.LogInformation("Bulk insert...");
            // bulk insert for Npgsql, cf.: https://github.com/PostgreSQLCopyHelper/PostgreSQLCopyHelper
            var copyHelper = new PostgreSQLCopyHelper<Condition>("conditions")
                .MapTimeStampTz("time", x => x.Time)
                .MapCharacter("device_id", x => x.DeviceId)
                .MapDouble("temperature", x => x.Temperature)
                .MapDouble("humidity", x => x.Humidity);
            await using var connection = new NpgsqlConnection(_context.Database.GetDbConnection().ConnectionString);
            connection.Open();
            var count = await copyHelper.SaveAllAsync(connection, conditions);

            return Ok(count);
        }
    }
}
