# https://hub.docker.com/_/microsoft-dotnet-core-aspnet/
FROM mcr.microsoft.com/dotnet/aspnet:6.0 as base

FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build
COPY . /src/
WORKDIR /src/

FROM build AS publish
RUN dotnet publish -c Release -o /app

FROM base AS final
EXPOSE 80

WORKDIR /app
COPY --from=publish /app .
ENTRYPOINT ["dotnet", "webapi.dll"]
