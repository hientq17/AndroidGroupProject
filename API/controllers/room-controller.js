'use strict'

const responseBaseModel = require("../models/response-base-model")
const storeProcedureName = require("../configs/store-procedure-name")
const isValidModel = require("../utils/model-filter")
const cloudinary = require("../configs/cloudinary")

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
                        res.json(response[0][0])
                })
            }
            else {
                res.json("EMPTY")
            }
        },
        insertOrUpdateRoom: async (req, res) => {
            let data = req.body;
            const [url1, url2, url3] = await Promise.all([uploadImage(data.JInput.image1), uploadImage(data.JInput.image2), uploadImage(data.JInput.image3)]);
            data.JInput.image1 = data.JInput.image2 = data.JInput.image3 = "";
            data.JInput.image = url1+";"+url2+";"+url3;
            console.log(data);
            dbConnection.query(storeProcedureName().insertOrUpdateRoom(), [JSON.stringify(data.JInput),data.Action], (err, response) => {
                let resData = response[0]
                let returnId = resData[0].returnId
                let outputMessage = resData[0].outputMessage
                let model = new responseBaseModel(returnId, outputMessage=="SUCCESS"?true:false, outputMessage)
                if (err) res.json(err)
                res.json(model)
            })
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

const uploadImage = async (base64) => {
    let url = "";
    try {
        if(base64.length==0) return url;
        if(base64.startsWith("http")) return base64;
        await cloudinary.uploader.upload(base64, {
            resource_type: "image",
            use_filename: "true",
            folder: "ftro"
        }, async function(error, result) {
            if (error) console.log(error)
            else {
                console.log(result);
                url = result.url;
            }
        });
    } catch (err) {
        console.error(err);
    }
    return url;
}

module.exports = apiRoom