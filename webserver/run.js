var http = require('http');
var url = require('url');
var upload = require('./cloudStorage/upload');
var deleteFile = require('./cloudStorage/delete.js');
var recognize = require('./speechRecognition/recognize.js');

function getTranscript(f, success, error) {
  console.log('do it', f);
  upload(
    f,
    function (filename) {
      recognize(filename, 'AMR', 8000, 'en-US', function(data) {
        success(data);
	// deleteFile(filename);
      });
    },
    function (err) {
      console.log(err);
      error(err);
    }
  );  
}

http.createServer(function (req, res) {
  var filename = url.parse(req.url, true).query.filename;
  
  if (filename) {
    getTranscript(filename, (data) => {
      res.writeHead(200, {'Content-Type': 'text/plain'});
      res.end(data);
    }, () => {
      res.writeHead(500, {'Content-Type': 'text/plain'});
      res.end(error);
    });
  } else {
    res.writeHead(401, {'Content-Type': 'text/plain'});
    res.end('No file');
  }
}).listen(8888);
