// Wait till the browser is ready to render the game (avoids glitches)
window.requestAnimationFrame(function () {
  new GameManager(4, KeyboardInputManager, HTMLActuator, LocalStorageManager);

  document.getElementById('share').onclick = function(){
      window.android.share('Inverter中文版拼图','Inverter中文版拼图-快来玩吧：http://www.wandoujia.com/apps/com.hanxi.inverter ','给好友分享');
  };

  document.getElementById('showAdsAppWall').onclick = function(){
      window.android.showAdsAppWall();
  };

  document.getElementById('sourceCode').onclick = function(){
      window.location.href="https://github.com/MaartenBaert/2048";
  };

});
