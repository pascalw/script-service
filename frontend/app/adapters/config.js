import ApplicationAdapter from './application';

export default ApplicationAdapter.extend({
  createRecord: function(store, type, record) {
    // id's are client-generated, so create our record by PUTting it
    return this.updateRecord(store, type, record);
  }
});
