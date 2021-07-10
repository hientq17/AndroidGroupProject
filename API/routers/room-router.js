'use strict';

module.exports = function (app, dbConnection) {
    let roomController = require('../controllers/room-controller')

    app.route('/api/room/get-list-rooms-by-author')
        .get(roomController(dbConnection).getListRoomsByAuthor);

    app.route('/api/room/get-room-by-id')
        .get(roomController(dbConnection).getRoomById);

    app.route('/api/room/insert-or-update-room')
        .post(roomController(dbConnection).insertOrUpdateRoom);

    app.route('/api/room/delete-room')
        .post(roomController(dbConnection).deleteRoom);
        
};