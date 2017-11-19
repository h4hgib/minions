var fs = require('fs');
var storage = require('@google-cloud/storage')();
var myBucket = storage.bucket('caller-record-storage');

function upload (filename, success, error) {
  var filename = filename;
  var file = myBucket.file(filename);

  fs.createReadStream('./resources/' + filename)
    .pipe(file.createWriteStream())
    .on('error', function(err) {
      error(err);
    })
    .on('finish', function() {
      success('gs://caller-record-storage/' + filename);
    });
}

module.exports = upload;
