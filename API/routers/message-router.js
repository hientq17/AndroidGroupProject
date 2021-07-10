'use strict';

module.exports = function (app, dbConnection) {
    let messageController = require('../controllers/message-controller')

    app.route('/api/message/get-list-messages-by-inbox')
        .get(messageController(dbConnection).getListMessagesByInbox);

    app.route('/api/message/get-message-by-id')
        .get(messageController(dbConnection).getMessageById);

    app.route('/api/message/insert-or-update-message')
        .post(messageController(dbConnection).insertOrUpdateMessage);

    app.route('/api/message/delete-message')
        .post(messageController(dbConnection).deleteMessage);
        
};