var admin = require('../models/Admin');
var beacon = require('./models/Beacon');

module.exports = {
  configure: function(app) {
    app.get('/admin/', function(req, res) {
      admin.get(res);
    });
    app.get('/beacon/:id/', function(req, res) {
      beacon.get(req.params.id,res);
    });
  }
};
