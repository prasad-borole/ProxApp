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


  this.authenticate = function(req, res) {
    connection.acquire(function(err, con) {
      console.log(req);
      con.query('select * from Admin where email_id= ?', [req.username], function(err, result) {
        con.release();
        if (err) {
          res.send({status: 1, message: 'User Authentication failed'});
        } else {
          //res.send(result.password);
          if(req.password===result[0].password)
             res.send({status: 0, message: 'User Authenticated successfully'});
          else
             res.send({status: -1, message: "Password doesnot match"});
        }
      });
    });
  };

  this.register = function(req, res) {
    connection.acquire(function(err, con) {
      console.log(req);
      var insert_ele = {email_id: req.username, name: "default", password: req.password};
      con.query('insert into Admin set ?', insert_ele, function(err, result) {
      con.release();
        if (err) {
           res.send({status: 1, message: 'User creation failed'});
        } else {
           res.send({status: 0, message: 'User created successfully'});
        }
     });
    });
  };

}
module.exports = new Admin();
