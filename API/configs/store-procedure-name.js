'use strict'

const storeProcedureName = function () {
    return {
        //Room
        getListAllRooms: () => {
            return 'Call getListAllRooms()'
        },
        getTopRooms: () => {
            return 'Call getTopRooms()'
        },
        getListBookedRoomsByUsername: () => {
            return 'Call getListBookedRoomsByUsername(?)'
        },
        getListRoomsByAuthor: () => { 
            return 'Call getListRoomsByAuthor(?)'
        },
        getRoomById: () => {
            return 'Call getRoomById(?)'
        },
        insertOrUpdateRoom: () => {
            return 'Call insertOrUpdateRoom(?,?)'
        },
        deleteRoom: () => {
            return 'Call deleteRoom(?)'
        },
        //Message
        getListMessagesByInbox: () => { 
            return 'Call getListMessagesByInbox(?)'
        },
        getMessageById: () => {
            return 'Call getMessageById(?)'
        },
        insertOrUpdateMessage: () => {
            return 'Call insertOrUpdateMessage(?,?)'
        },
        deleteMessage: () => {
            return 'Call deleteMessage(?)'
        },
        //User
        login: () => {
            return 'Call login(?)'
        },
        signup: () => {
            return 'Call signup(?)'
        },
        getUserByUsername: () => {
            return 'Call getUserByUsername(?)'
        },
        //Book
        getListBooksByRoom: () => { 
            return 'Call getListBooksByRoom(?)'
        },
        getBookById: () => {
            return 'Call getBookById(?)'
        },
        insertOrUpdateBook: () => {
            return 'Call insertOrUpdateBook(?,?)'
        },
        deleteBook: () => {
            return 'Call deleteBook(?)'
        },
        //Inbox
        getListInboxsByAuthor: () => { 
            return 'Call getListInboxsByAuthor(?)'
        },
        getInboxById: () => {
            return 'Call getInboxById(?)'
        },
        insertOrUpdateInbox: () => {
            return 'Call insertOrUpdateInbox(?,?)'
        },
        deleteInbox: () => {
            return 'Call deleteInbox(?)'
        }
    }
}

module.exports = storeProcedureName