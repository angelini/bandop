class Bandop.Algorithm extends Bandop.Model
  @resourceName: 'algorithm'

  @encode 'name', 'config', 'type'

  @belongsTo 'experiment', autoload: false

class Bandop.AlgorithmType extends Bandop.Model
  @resourceName: 'algorithm_type'

  @encode 'name', 'defaults'
