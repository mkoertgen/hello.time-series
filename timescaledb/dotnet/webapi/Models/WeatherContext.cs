using System;
using System.ComponentModel.DataAnnotations;
using Microsoft.EntityFrameworkCore;
using NpgsqlTypes;

namespace webapi.Models
{
    public class WeatherContext : DbContext
    {
        public DbSet<Condition> Conditions { get; set; }
        public WeatherContext(DbContextOptions<WeatherContext> options) : base(options)
        {
            // cf.: https://docs.microsoft.com/en-us/ef/core/miscellaneous/cli/dotnet
            // dotnet tool install --global dotnet-ef
            // dotnet ef migrations add/remove
            // dotnet ef database update / drop
        }

        // note that we need a composite primary key, bc.:
        // 1. efcore DbSet really wants a PK
        // 2. TimeScaleDb needs an index on 'time'
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Condition>()
                .HasKey(c => new { c.Time, c.DeviceId });
        }
    }

    public class Condition
    {
        // weather model, cf.:
        // - https://docs.timescale.com/latest/getting-started/creating-hypertables
        // - https://www.npgsql.org/doc/types/basic.html
        [Key]
        public DateTimeOffset Time { get; set; }
        [Key] // for distributed hyper_tables
        public string DeviceId { get; set; }
        public NpgsqlPoint? Location { get; set; }
        public double? Temperature { get; set; }
        public double? Humidity { get; set; }
    }
}
