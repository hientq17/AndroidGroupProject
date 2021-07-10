'use strict';

module.exports = function (app, dbConnection) {
    let inboxController = require('../controllers/inbox-controller')

    app.route('/api/inbox/get-list-inboxs-by-author')
        .get(inboxController(dbConnection).getListInboxsByAuthor);

    app.route('/api/inbox/get-inbox-by-id')
        .get(inboxController(dbConnection).getInboxById);

    app.route('/api/inbox/insert-or-update-inbox')
        .post(inboxController(dbConnection).insertOrUpdateInbox);

    app.route('/api/inbox/delete-inbox')
        .post(inboxController(dbConnection).deleteInbox);
        
};