var connection = require('../connection');

function Admin() {
  this.get = function(res) {
    connection.acquire(function(err, con) {
      con.query('select * from Admin', function(err, result) {
        con.release();
        res.send(result);
      });
    });
  };
}

module.exports = new Admin();
