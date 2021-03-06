﻿// <auto-generated />
using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;
using NpgsqlTypes;
using webapi.Models;

namespace webapi.Migrations
{
    [DbContext(typeof(WeatherContext))]
    [Migration("20200106093858_Initial")]
    partial class Initial
    {
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn)
                .HasAnnotation("ProductVersion", "3.1.0")
                .HasAnnotation("Relational:MaxIdentifierLength", 63);

            modelBuilder.Entity("webapi.Models.Condition", b =>
                {
                    b.Property<DateTimeOffset>("Time")
                        .HasColumnName("time")
                        .HasColumnType("timestamp with time zone");

                    b.Property<string>("DeviceId")
                        .HasColumnName("device_id")
                        .HasColumnType("text");

                    b.Property<double?>("Humidity")
                        .HasColumnName("humidity")
                        .HasColumnType("double precision");

                    b.Property<NpgsqlPoint?>("Location")
                        .HasColumnName("location")
                        .HasColumnType("point");

                    b.Property<double?>("Temperature")
                        .HasColumnName("temperature")
                        .HasColumnType("double precision");

                    b.HasKey("Time", "DeviceId");

                    b.ToTable("conditions");
                });
#pragma warning restore 612, 618
        }
    }
}
