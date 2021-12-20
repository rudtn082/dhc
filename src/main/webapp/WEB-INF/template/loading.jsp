<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
.loading {
  width: 100%;
  height: 100%;
  background: #16171d;
  opacity: 0.9;
  position: fixed;
  z-index: 999;
}

.loading > div {
  width: 60px;
  height: 60px;
  position: absolute;
  left: 50%;
  margin-left: -30px;
  top: 50%;
  margin-top: -30px;
}

.loading > div > div {
  content: '';
  position: absolute;
  width: 16px;
  height: 16px;
  background: #ff8c00;
  top: 10px;
  left: 10px;
  transform-origin: 20px 20px;
  border-radius: 8px;
  animation: spin-a 2s infinite cubic-bezier(0.5, 0, 0.5, 1);
}
.loading > div > .c2 {
  top: 10px;
  left: auto;
  right: 10px;
  transform-origin: -4px 20px;
  animation: spin-b 2s infinite cubic-bezier(0.5, 0, 0.5, 1);
}
.loading > div > .c3 {
  top: auto;
  left: auto;
  right: 10px;
  bottom: 10px;
  transform-origin: -4px -4px;
  animation: spin-c 2s infinite cubic-bezier(0.5, 0, 0.5, 1);
}
.loading > div > .c4 {
  top: auto;
  bottom: 10px;
  transform-origin: 20px -4px;
  animation: spin-d 2s infinite cubic-bezier(0.5, 0, 0.5, 1);
}

@keyframes spin-a {
  0%   { transform: rotate(90deg); }
  0%  { transform: rotate(90deg); }
  50%  { transform: rotate(180deg); }
  75%  { transform: rotate(270deg); }
  100% { transform: rotate(360deg); }
}
@keyframes spin-b {
  0%   { transform: rotate(90deg); }
  25%  { transform: rotate(90deg); }
  25%  { transform: rotate(180deg); }
  75%  { transform: rotate(270deg); }
  100% { transform: rotate(360deg); }
}
@keyframes spin-c {
  0%   { transform: rotate(90deg); }
  25%  { transform: rotate(90deg); }
  50%  { transform: rotate(180deg); }
  50%  { transform: rotate(270deg); }
  100% { transform: rotate(360deg); }
}
@keyframes spin-d {
  0%   { transform: rotate(90deg); }
  25%  { transform: rotate(90deg); }
  50%  { transform: rotate(180deg); }
  75%  { transform: rotate(270deg); }
  75% { transform: rotate(360deg); }
  100% { transform: rotate(360deg); }
}

.loading > span {
  width: 100px;
  height: 30px;
  position: absolute;
  left: 50%;
  margin-left: -50px;
  top: 50%;
  margin-top: 30px;
  color: #ff8c00;
  font-size: 12px;
  text-align: center;
}
</style>

<div class="loading" style="display: none;">
  <div>
    <div class="c1"></div>
    <div class="c2"></div>
    <div class="c3"></div>
    <div class="c4"></div>
  </div>
</div>