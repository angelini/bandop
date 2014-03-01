class Bandop.Algorithm extends Bandop.Model
  @resourceName: 'algorithm'

  @encode 'name', 'config'

  @belongsTo 'experiment', autoload: false
