using System;
using Microsoft.EntityFrameworkCore.Migrations;
using NpgsqlTypes;
using webapi.Models;

namespace webapi.Migrations
{
    public partial class Initial : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "conditions",
                columns: table => new
                {
                    time = table.Column<DateTimeOffset>(nullable: false),
                    device_id = table.Column<string>(nullable: false),
                    temperature = table.Column<double>(nullable: true),
                    humidity = table.Column<double>(nullable: true),
                    location = table.Column<NpgsqlPoint>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_conditions", x => new { x.time, x.device_id });
                });

            migrationBuilder.CreateHyperTable("conditions", "time");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "conditions");
        }
    }
}
