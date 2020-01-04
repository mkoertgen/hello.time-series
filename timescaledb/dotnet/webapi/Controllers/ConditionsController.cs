using System;
using System.Collections.Generic;
using System.Linq;
using DaleNewman;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
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
        public IEnumerable<Condition> Get(string from="now-15m", string to="now", int size=10, int page=0)
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
    }
}
