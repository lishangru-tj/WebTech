const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  age:state=>state.user.age,
  introduce:state=>state.user.introduce,
  id:state=>state.user.id,
  email:state=>state.user.email,
  roles:state=>state.user.roles,
  permission_routes: state => state.permission.routes
}
export default getters