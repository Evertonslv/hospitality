FROM node:20-alpine

WORKDIR /opt/project

COPY package*.json ./

RUN npm install

RUN npm install -g @angular/cli
RUN apk add --no-cache bash

COPY . ./

CMD [ "sh", "-c", "[ -d node_modules ] && npm run start || npm ci -q && npm run start" ]
