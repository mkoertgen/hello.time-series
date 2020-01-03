using Microsoft.EntityFrameworkCore.Migrations;

namespace webapi.Models
{
    static class TimeScaleDbExtensions
    {
        public static MigrationBuilder CreateHyperTable(this MigrationBuilder migrationBuilder,
            string tableName, string timeColumnName)
        {
            // on hypertables, cf.:
            // - https://docs.timescale.com/latest/getting-started/creating-hypertables
            // - https://docs.timescale.com/latest/using-timescaledb/hypertables
            // - https://docs.timescale.com/latest/api#create_hypertable
            migrationBuilder.Sql($"SELECT create_hypertable('{tableName}', '{timeColumnName}');");
            return migrationBuilder;
        }

        public static MigrationBuilder CreateDistributedHyperTable(this MigrationBuilder migrationBuilder,
            string tableName, string timeColumnName, string partitionColumnName)
        {
            // cf.: https://blog.timescale.com/blog/building-a-distributed-time-series-database-on-postgresql/
            migrationBuilder.Sql($"SELECT create_distributed_hypertable('{tableName}', '{timeColumnName}', '{partitionColumnName}');");
            return migrationBuilder;
        }
    }
}
