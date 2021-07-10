'use strict';

module.exports = function (app, dbConnection) {
  const roomRouter = require('./room-router')
  roomRouter(app, dbConnection);
  const messageRouter = require('./message-router')
  messageRouter(app, dbConnection);
  const userRouter = require('./user-router')
  userRouter(app, dbConnection);
  const bookRouter = require('./book-router')
  bookRouter(app, dbConnection);
  const inboxRouter = require('./inbox-router')
  inboxRouter(app, dbConnection);
};
