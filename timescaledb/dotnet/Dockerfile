# https://hub.docker.com/_/microsoft-dotnet-core-aspnet/
FROM mcr.microsoft.com/dotnet/core/aspnet:3.1.0-alpine3.10 as base

FROM mcr.microsoft.com/dotnet/core/sdk:3.1.100-alpine3.10 AS build
COPY . /src/
WORKDIR /src/

FROM build AS publish
RUN dotnet publish -c Release -o /app

FROM base AS final
EXPOSE 80

WORKDIR /app
COPY --from=publish /app .
ENTRYPOINT ["dotnet", "webapi.dll"]
