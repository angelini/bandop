class Bandop.User extends Batman.Model
  @urlPrefix: '/api'
  @resourceName: 'user'

  @persist Batman.RestStorage

  @encode 'email', 'domain'

class Bandop.UsersController extends Batman.Controller
  routingKey: 'users'

  login: (params) ->
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

    Batman.redirect(Bandop.get('routes.designs').path())

  loginFailure: (request) =>
    @set('loginError', request.responseJSON?.message)
