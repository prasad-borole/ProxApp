var admin = require('../models/Admin');
var beacon = require('../models/Beacon');

module.exports = {
  configure: function(app) {
    app.get('/admin/', function(req, res) {
      admin.get(res);
    });
    app.get('/beacon/:id', function(req, res) {
      beacon.get(req.params.id,res);
    });
    app.post('/beacon', function(req, res) {
      beacon.create(req.body, res);
    });
    app.post('/getBeacons', function(req, res) {
      beacon.beaconsList(req.body, res);
    });
    app.post('/login', function(req, res) {
      admin.authenticate(req.body, res);
    });
    app.post('/signup', function(req, res) {
      admin.register(req.body, res);
    });
  }
};
