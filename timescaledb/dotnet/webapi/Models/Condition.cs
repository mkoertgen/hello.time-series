using System;
using System.ComponentModel.DataAnnotations;

namespace webapi.Models
{
    public class Condition
    {
        // weather model, cf.:
        // - https://docs.timescale.com/latest/getting-started/creating-hypertables
        // - https://www.npgsql.org/doc/types/basic.html
        // - https://docs.timescale.com/latest/tutorials/other-sample-datasets
        // - https://docs.timescale.com/latest/using-timescaledb/reading-data
        [Key]
        public DateTimeOffset Time { get; set; }
        [Key] // for distributed hyper_tables
        public string DeviceId { get; set; }
        public double? Temperature { get; set; } // Fahrenheit
        public double? Humidity { get; set; }
    }
}
