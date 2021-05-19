const functions = require("firebase-functions");

var {google} = require('googleapis');
var MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
var SCOPES = [MESSAGING_SCOPE];

var express = require('express');
var app = express(); 
var bodyParser = require('body-parser');
var router = express.Router(); 
var request = require('request');
const { topic } = require("firebase-functions/lib/providers/pubsub");

app.use(bodyParser.urlencoded({extended:true}));
app.use(bodyParser.json());

router.post('/send', function(req, res){
    console.log("Entered here")
    
    getAccessToken().then(function(access_token){
        console.log(access_token);

        var topic = req.body.topic; 
        var body = req.body.body; 
        var title = req.body.title; 
        var articleId = req.body.articleId; 

        request.post({
            headers:{
                Authorization: 'Bearer '+access_token
            }, 
            url: "https://fcm.googleapis.com/v1/projects/pocketnews-277/messages:send", 
            body: JSON.stringify(
                {
                    "message":{
                        "topic": topic,
                        "notification" : {
                            "body" : body,
                            "title" : title,
                        },
                        "data": {
                            "articleId": articleId
                        }
                    }
                }
            )
        }, function(error, response, body){
            res.end(body);
            console.log(body);
        });
    });
});

app.use('/api', router);


function getAccessToken(){
    return new Promise(function(resolve, reject){
        var key = require("./service-account.json");
        var jwtClient = new google.auth.JWT(
            key.client_email,
            null,
            key.private_key,
            SCOPES,
            null
        );
        jwtClient.authorize(function(err, tokens){
            if(err){
                reject(err);
                return; 
            }
            resolve(tokens.access_token);
        });
    });
}


exports.api = functions.https.onRequest(app);
