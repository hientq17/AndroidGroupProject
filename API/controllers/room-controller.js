'use strict'

const responseBaseModel = require("../models/response-base-model")
const storeProcedureName = require("../store_procedures/store-procedure-name")
const isValidModel = require("../utils/model-filter")

const apiRoom= function (dbConnection) {
    return {
        getListAllRooms: (req,res) => {
            dbConnection.query(storeProcedureName().getListAllRooms(),null, (err, response) => {
                if (err) res.json(err)
                else{
                    if(response[0].length > 0)
                         res.json(response[0])
                    else
                        //res.json("EMPTY")
                        res.json([])
                }
            })
        },
        getTopRooms: (req,res) => {
            dbConnection.query(storeProcedureName().getTopRooms(),null, (err, response) => {
                if (err) res.json(err)
                else{
                    if(response[0].length > 0)
                         res.json(response[0])
                    else
                        //res.json("EMPTY")
                        res.json([])
                }
            })
        },
        getListBookedRoomsByUsername: (req, res) => {
            dbConnection.query(storeProcedureName().getListBookedRoomsByUsername(),[req.query.username], (err, response) => {
                if (err) res.json(err)
                else{
                    if(response[0].length > 0)
                         res.json(response[0])
                    else
                        //res.json("EMPTY")
                        res.json([])
            
                }
            })
        },
        getListRoomsByAuthor: (req, res) => {
            dbConnection.query(storeProcedureName().getListRoomsByAuthor(),[req.query.author], (err, response) => {
                if (err) res.json(err)
                else{
                    if(response[0].length > 0)
                         res.json(response[0])
                    else
                        //res.json("EMPTY")
                        res.json([])
                }
            })
        },
        getRoomById: (req, res) => {
            if (req.query.id > 0) {
                dbConnection.query(storeProcedureName().getRoomById(), [req.query.id], (err, response) => {
                    if (err) res.json(err);
                    if (response[0].length == 0)
                        //res.json("EMPTY")
                        res.json(null)
                    else
                        res.json(response[0])
                })
            }
            else {
                res.json("EMPTY")
            }
        },
        insertOrUpdateRoom: (req, res) => {
            if (isValidModel(req.body)) {
                let data = req.body;
                dbConnection.query(storeProcedureName().insertOrUpdateRoom(), [JSON.stringify(data.JInput),data.Action], (err, response) => {
                    let resData = response[0]
                    let returnId = resData[0].returnId
                    let outputMessage = resData[0].outputMessage
                    let model = new responseBaseModel(returnId, outputMessage=="SUCCESS"?true:false, outputMessage)
                    if (err) res.json(err)
                    res.json(model)
                })
            }
            else {
                res.json("Invalid model")
            }
        },
        deleteRoom: (req, res) => {
            if (req.query.id > 0 ) {
                dbConnection.query(storeProcedureName().deleteRoom(), [req.query.id], (err, response) => {
                    let resData = response[0]
                    let returnId = resData[0].returnId
                    let outputMessage = resData[0].outputMessage
                    let model = new responseBaseModel(returnId, outputMessage=="SUCCESS"?true:false, outputMessage)
                    if (err) res.json(err)
                    res.json(model)
                })
            }
            else {
                //res.json("EMPTY")
                res.json(null)
            }
        },
        getSearchRoom: (req,res) => {
            dbConnection.query(storeProcedureName().searchRoom(),[req.query.searchText], (err, response) => {
                if (err) res.json(err)
                else{
                    console.log(response)
                    if(response[0].length > 0)
                         res.json(response[0])
                    else
                        //res.json("EMPTY")
                        res.json([])
                }
            })
        }
        // testFormFile:(req, res) => {
        //     console.log(req.files)
        //     res.json("1")
        // }
    }
}

module.exports = apiRoom