var fs = require('fs');
var storage = require('@google-cloud/storage')();
var myBucket = storage.bucket('caller-record-storage');

function deleteFile (filename, success, error) {
  var filename = filename;
  var file = myBucket.file(filename);
  file.delete(error, success);
}

module.exports = deleteFile;