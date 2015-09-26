import ApplicationRoute from './application';

export default ApplicationRoute.extend({
  model() {
    return this.store.findAll('endpoint');
  }
});
