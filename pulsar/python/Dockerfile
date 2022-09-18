ARG BASE_IMAGE=python:3.10.7-slim-bullseye
# hadolint ignore=DL3006
FROM ${BASE_IMAGE}
WORKDIR /usr/src/app
COPY requirements.txt ./
RUN pip install -r requirements.txt
COPY . .
CMD ["python", "consumer.py"]
