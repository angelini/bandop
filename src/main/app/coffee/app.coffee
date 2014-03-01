class Bandop extends Batman.App
  @root 'experiments#index'

  @resources 'experiments'
  @resources 'designs', only: ['show', 'new']

  @route 'login', 'users#login'
  @route 'logout', 'users#logout'

  @alert: (message) ->
    @set('alertMessage', message)
    $('html').animate(scrollTop: 0)

  @dissmissAlert: ->
    @unset('alertMessage')

class Bandop.RestStorage extends Batman.RestStorage
  serializeAsForm: false
  recordJsonNamespace: -> false

class Bandop.Model extends Batman.Model
  @urlPrefix: '/api'
  urlPrefix: '/api'

  @persist Bandop.RestStorage

class Bandop.Controller extends Batman.Controller
  @catchError Batman.StorageAdapter.UnauthorizedError, with: 'redirectLogin'
  @afterAction -> Bandop.dissmissAlert()

  @beforeAction ->
    if !Bandop.get('currentUser') && $.cookie('_bandop_login')
      Bandop.apiRequest
        method: 'GET'
        url: 'auth/current'
        success: @setCurrentUser
        error: @redirectLogin

  setCurrentUser: (user) ->
    Bandop.set('currentUser', new Bandop.User(user))

  redirectLogin: ->
    Batman.redirect
      controller: 'users'
      action: 'login'
      redirectController: @routingKey
      redirectAction: @action
      redirectId: @params.id

Bandop.apiRequest = (options) ->
  new Batman.Request
    method: options.method || 'GET'
    contentType: 'application/json'
    url: '/api/' + options.url
    data: JSON.stringify(options.data)
    success: options.success
    error: options.error

$.cookie.raw = true

Batman.View::cache = false
Batman.config.usePushState = false
Batman.config.pathToHTML = '/assets/html'

window.Bandop = Bandop
