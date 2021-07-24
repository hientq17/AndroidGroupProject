'use strict';

module.exports = function (app, dbConnection) {
    let bookController = require('../controllers/book-controller')

    app.route('/api/book/get-list-books-by-room')
        .get(bookController(dbConnection).getListBooksByRoom);

    app.route('/api/book/get-book-by-id')
        .get(bookController(dbConnection).getBookById);

    app.route('/api/book/insert-or-update-book')
        .post(bookController(dbConnection).insertOrUpdateBook);

    app.route('/api/book/delete-book')
        .post(bookController(dbConnection).deleteBook);
        
    app.route('/api/book/get-list-user-booking')
        .get(bookController(dbConnection).getListUserBooking);
        
};