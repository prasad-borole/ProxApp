var connection = require('../connection');

function Beacon() {
  this.get = function(id, res) {
    connection.acquire(function(err, con) {
      //console.log(id);
      con.query('select * from BeaconInfo where beacon_id= ?', [id], function(err, result) {
        con.release();
        if (err) {
          res.send(err);
        } else {
          res.send(result);
        }
      });
    });
  };

  this.create = function(beacon, res) {
    connection.acquire(function(err, con) {
      console.log(beacon);
      con.query('insert into BeaconInfo set ?', beacon, function(err, result) {
        con.release();
        if (err) {
          res.send({status: 1, message: 'beacon creation failed'});
        } else {
          res.send({status: 0, message: 'beacon created successfully'});
        }
      });
    });
  };

  this.beaconsList = function(email, res) {
    connection.acquire(function(err, con) {
      console.log(email);
      con.query('select * from BeaconInfo where beacon_id in (select beacon_id from Beacon where email_id =?)', email.email_id,
      function(err, result) {

        if (err) {
          res.send(err);
        } else {
          res.send(result);
        }
      });
    });
  };

}

module.exports = new Beacon();
