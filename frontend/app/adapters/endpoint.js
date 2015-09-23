import ApplicationAdapter from './application';

export default ApplicationAdapter.extend({
  pathForType: function(modelName) {
    // from the server perspective we're CRUDing endpoint configurations.
    // from the clients perspective we just call this endpoints.
    return 'configs';
  },

  createRecord: function(store, type, record) {
    // id's are client-generated, so create our record by PUTting it
    return this.updateRecord(store, type, record);
  }
});
