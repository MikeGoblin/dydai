// pages/home/home.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    pages:0,
    swiperlist:[
      {image:"/images/01swiperImg.png"},
      {image:"/images/02swiperImg.png"},
      {image:"/images/03swiperImg.png"}
    ],
    maxnum:5,
    token:'',
    isUp:'true',
    userInfo:[],
    orderNum:0,
    orderList:[
      /*{
        title:'不想被饿的挣扎',
        orderid:114514,
        value:12.25,
        phone:'110',
        target_address:'电子科技大学沙河校区菜鸟驿站',
        orderer_address:'第二教学楼104',
        ordertime:'2003.03.24',
        content:'我快要饿死，然后给我带包辣条谢谢'
      }*/
    ]
  },
  postUserInfo(){
    wx.getStorage({
      key: 'token',
      success: (res) => {
        //console.log(res.data);
        var uname = this.data.userInfo.nickName;
        var atar = this.data.userInfo.avatarUrl;
        wx.request({
          url: 'https://api.revocat.tech/user/update-info',
          method:'POST',
          //这里选择把userinfo全部都给后端
          data:{
            token:res.data,
            username:uname,
            avatar:atar
          },
          success:(res)=>{
            //返回要事先判断成功与失败
            //console.log(res);
            if(res.data.success==0){
              console.log(res.data.msg);
            }else{
            }
          }
        })
      }
    })
  },
  allow(){
    wx.getUserProfile({
      desc: '你的授权信息',
      success: (res) => {
        wx.setStorage({
          key:'userInfo',
          data:res.userInfo
        }) 
        this.setData({
          userInfo:res.userInfo
        })
        wx.setStorage({
          key:'isUp',
          data:'false'
        })
        this.setData({
          isUp:'false'
        })
        this.postUserInfo();
      }
    })
  },
  hide(){
    this.setData({
      isUp:'false'
    })
  },
  myLogin(){
    wx.login({
      success: (res) => {
        //拉取的所有信息一律存储到本地
        /*wx.setStorage({
          key: 'code',
          data: res.code
        })*/
        //console.log(res.code);
        //发post请求 获取token并存入本地
        wx.request({
          url: 'https://api.revocat.tech/user/login',
          method:'POST',
          data:{
            code:res.code
          },
          success:(res)=>{
            if(res.data.success==0){
              console.log(res.data.msg);
            }else{
              wx.setStorageSync('token', res.data.token)
              this.onShow();
              };
          }
        })
        //进行一次用户信息的拉取
      },
    });
  },
  /*var token = wx.getStorageSync('token');
              wx.request({
                url: 'https://api.revocat.tech/order/get-all',
                method: 'POST',
                data: {
                  token: token,
                  maxnum:this.data.maxnum,
                  page:this.data.pages
                },
                success: (res) => {
                  //console.log(res);
                  if(res.data.success==0){
                    this.setData({
                      pages:prpage
                    })
                    console.log(res.data.msg);
                  }else{
                    console.log("我这里调用了一次增添");
                    var list = this.data.orderList;
                    var tarlist = res.data.orders;
                    //console.log(tarlist);
                    for(var i =0;i< tarlist.length;i++){
                      list.push(tarlist[i])
                    }
                    this.setData({
                      orderNum:res.count,
                      orderList:list
                    })
                  }
                }
              })*/
  //用于拉取轮播图数据
  getSwiperList(){
    wx.request({
      url: 'https://www.escook.cn/slides',
      method:'GET',
      success:(res)=>{
        console.log(res);
        this.setData({
          swiperlist:res.data
        })
      }
    })
  },
  /*startInter : function(){
    setInterval(
        function () {
          
        }, 1000);    
  },*/
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.myLogin();
  },
  
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {
    //this.SearchUsableOrder()
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    this.setData({
      pages:0
    })
    wx.getStorage({
      key:'isUp',
      success:(res)=>{
        this.setData({
          isUp:res.data
        })
      }      
    })
    this.SearchUsableOrder()
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },
  gotoOrder(event){//传来了订单id建议订单id存本地，下个页面取出id再进行渲染
    wx.setStorage({
      key:'currentorderid',
      data:event.currentTarget.dataset.orderid
    })
    /*wx.getStorage({
      key:'currentorderid',
      success:(res)=>{
        console.log(res.data);
      }
    })*/
    wx.navigateTo({
      url:"/pages/order/order"
    })
  },
  gotoCreate(){
    wx.navigateTo({
      url: '/pages/create/create',
    })
  },
  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {
    //用于拉取5个订单数据
    //SearchUsableOrder
    console.log(123);
    var page = this.data.pages;
    var prpage = page;
    page++;
    this.setData({
      pages:page
    })
    console.log(this.data.pages);
    wx.getStorage({
      key: 'token',
      success: (res) => {
        wx.request({
          url: 'https://api.revocat.tech/order/get-all',
          method: 'POST',
          data: {
            token: res.data,
            maxnum:this.data.maxnum,
            page:this.data.pages
          },
          success: (res) => {
            //console.log(res);
            if(res.data.success==0){
              this.setData({
                pages:prpage
              })
              console.log(res.data.msg);
            }else{
              var list = this.data.orderList;
              var tarlist = res.data.orders;
              //console.log(tarlist);
              for(var i =0;i< tarlist.length;i++){
                list.push(tarlist[i])
              }
              this.setData({
                orderNum:res.count,
                orderList:list
              })
            }
          }
        })
      }
    })
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },
  //用于拉取订单信息
  SearchUsableOrder(){
    //原始位置1
    wx.getStorage({
      key: 'token',
      success: (res) => {
        //问题在于我onshow时拉不出token来
        console.log("拉订单时成功获取了token一次");
        //console.log(res);
        //console.log(res);
        /*this.setData({
          token: res.data
        })*/
        wx.request({
          url: 'https://api.revocat.tech/order/get-all',
          method: 'POST',
          data: {
            token: res.data,
            maxnum:this.data.maxnum,
            page:this.data.pages
          },
          success: (res) => {
            //console.log(this.data.pages);
            //console.log(res);
            if(res.success==0){
              console.log(res.data.msg);
            }else{
              /*wx.setStorage({
                key:'orderNum',
                data:res.count
              })*/
              /*wx.setStorage({
                key:'orders',
                data:res.orders
              })*/
              this.setData({
                orderList:res.data.orders
              })
            }
          }
        })
      }
    })    
  },
})
