class Bandop.Design extends Batman.Model
  @urlPrefix: '/api'
  @resourceName: 'design'

  @persist Batman.RestStorage

  @encode 'name', 'cssFile', 'jsFile', 'screenshot'
  @belongsTo 'user', autoload: false

class Bandop.DesignsController extends Batman.Controller
  routingKey: 'designs'

  index: (params) ->
    if !Bandop.get('currentUser')
      return @redirect(Bandop.get('routes.login.path'))

    Bandop.Design.load (err, designs) =>
      @set('designs', designs)
