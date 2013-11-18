class Bandop.User extends Bandop.Model
  @resourceName: 'user'

  @encode 'email', 'domain'

class Bandop.UsersController extends Bandop.Controller
  routingKey: 'users'

  @accessor 'redirectPathHey', ->
    Bandop.get('dispatcher').pathFromParams
      id: @get('redirectId')
      action: @get('redirectAction') || 'index'
      controller: @get('redirectController') || 'designs'

  login: (params) ->
    if (params.redirectController)
      @set('redirectId', params.redirectId)
      @set('redirectAction', params.redirectAction)
      @set('redirectController', params.redirectController)
    else
      @unset('redirectId')
      @unset('redirectAction')
      @unset('redirectController')

    if $.cookie('_bandop_login')
      Bandop.apiRequest
        method: 'GET'
        url: 'auth/current'
        success: @loginSuccess
        error: @loginError

  logout: (params) ->
    $.cookie('_bandop_login', '')
    Bandop.unset('currentUser')
    Batman.redirect(Bandop.get('routes.login').path())

  submitLogin: (form, event, view) =>
    @set('loginError', null)

    Bandop.apiRequest
      method: 'POST'
      url: 'auth/login'
      data: {email: view.get('email'), password: view.get('password')}
      success: @loginSuccess
      error: @loginFailure

  loginSuccess: (user) =>
    Bandop.set('currentUser', new Bandop.User(user))
    $.cookie('_bandop_login', user.key) if user.key

    Batman.setImmediate => Batman.redirect(@get('redirectPathHey'))

  loginFailure: (request) =>
    @set('loginError', request.responseJSON?.message)
