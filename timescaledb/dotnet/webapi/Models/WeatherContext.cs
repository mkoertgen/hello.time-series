using Microsoft.EntityFrameworkCore;

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
        // 1. efcore DbSet really wants an id as PK
        // 2. TimeScaleDb needs an index on 'time'
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Condition>()
                .HasKey(c => new { c.Time, c.DeviceId });
        }
    }
}
