FROM node:8-alpine
WORKDIR /app
COPY . ./
ENV NODE_ENV=production
RUN yarn
RUN yarn build
EXPOSE 3000
ENTRYPOINT ["yarn","start"]