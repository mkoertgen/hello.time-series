FROM python:3.10.7-slim-bullseye
WORKDIR /usr/src/app
COPY requirements.txt ./
RUN pip install -r requirements.txt
COPY . .
CMD ["python3", "mqttgen.py", "config.json"]
