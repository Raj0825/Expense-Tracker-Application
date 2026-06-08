// ============ SHARED.JS — SpendSmart ============
const API = 'http://localhost:8080/api';

// AUTH HELPERS
function getToken(){ return localStorage.getItem('token'); }
function getUser(){ try{return JSON.parse(localStorage.getItem('user'));}catch{return null;} }
function requireAuth(){
  if(!getToken()){ window.location.href='/login.html'; return false; }
  return true;
}
function logout(){ localStorage.clear(); window.location.href='/login.html'; }

// API HELPER
async function apiFetch(path, opts={}){
  const headers = {'Content-Type':'application/json'};
  const token = getToken();
  if(token) headers['Authorization'] = `Bearer ${token}`;
  try {
    const res = await fetch(`${API}${path}`, { ...opts, headers:{...headers,...(opts.headers||{})} });
    if(res.status === 401){ logout(); return; }
    return await res.json();
  } catch(err) {
    console.error('API Error:', err);
    return { success: false, message: 'Server connection failed' };
  }
}

// FORMATTING
const SYMBOLS = {INR:'₹',USD:'$',EUR:'€',GBP:'£',JPY:'¥'};
function currSym(){ const u=getUser(); return SYMBOLS[u?.currency]||'₹'; }
function fmtMoney(n){
  const s=currSym(); n=Number(n)||0;
  if(n>=100000) return `${s}${(n/100000).toFixed(1)}L`;
  if(n>=1000) return `${s}${(n/1000).toFixed(1)}K`;
  return `${s}${n.toFixed(2)}`;
}
function fmtDate(d){ return new Date(d).toLocaleDateString('en-IN',{day:'2-digit',month:'short',year:'numeric'}); }
function relDate(d){
  const diff=Math.floor((Date.now()-new Date(d))/(1000*60*60*24));
  if(diff===0)return 'Today'; if(diff===1)return 'Yesterday';
  if(diff<7)return `${diff}d ago`;
  return fmtDate(d);
}

// CATEGORIES
const CATS = {
  food:{label:'Food & Dining',icon:'🍔',color:'#f97316'},
  travel:{label:'Travel',icon:'✈️',color:'#06b6d4'},
  clothes:{label:'Clothes',icon:'👗',color:'#ec4899'},
  bills:{label:'Bills & Utilities',icon:'⚡',color:'#f59e0b'},
  entertainment:{label:'Entertainment',icon:'🎬',color:'#8b5cf6'},
  health:{label:'Health & Fitness',icon:'💊',color:'#22c55e'},
  education:{label:'Education',icon:'📚',color:'#3b82f6'},
  shopping:{label:'Shopping',icon:'🛍️',color:'#e879f9'},
  housing:{label:'Housing & Rent',icon:'🏠',color:'#f43f5e'},
  personal:{label:'Personal Care',icon:'💆',color:'#14b8a6'},
  other:{label:'Other',icon:'📦',color:'#94a3b8'}
};
function getCat(v){ return CATS[v]||CATS.other; }
function catBadge(v){
  const c=getCat(v);
  return `<span class="badge" style="background:${c.color}18;color:${c.color};border:1px solid ${c.color}30">${c.icon} ${c.label}</span>`;
}

// TOAST
let toastTimer;
function showToast(msg,type='success'){
  let t=document.getElementById('globalToast');
  if(!t){ t=document.createElement('div'); t.id='globalToast'; t.className='toast'; document.body.appendChild(t); }
  t.className=`toast ${type}`;
  t.innerHTML=`<span>${type==='success'?'✓':'✕'}</span> ${msg}`;
  t.classList.add('show');
  clearTimeout(toastTimer);
  toastTimer=setTimeout(()=>t.classList.remove('show'),3000);
}

// SIDEBAR RENDER
function renderSidebar(active){
  const user=getUser()||{};
  const initial=(user.name||'U')[0].toUpperCase();
  const nav=[
    {id:'dashboard',icon:'📊',label:'Dashboard',href:'/dashboard.html'},
    {id:'expenses',icon:'💳',label:'Expenses',href:'/expenses.html'},
    {id:'add',icon:'➕',label:'Add Expense',href:'/add-expense.html'},
    {id:'reports',icon:'📈',label:'Monthly Reports',href:'/reports.html'},
    {id:'suggestions',icon:'💡',label:'Suggestions',href:'/suggestions.html'},
    {id:'settings',icon:'⚙️',label:'Settings',href:'/profile.html'},
  ];
  return `
  <aside class="sidebar">
    <div class="sidebar-logo">
      <div class="logo-icon">💸</div>
      <div class="logo-text">Spend<span>Smart</span></div>
    </div>
    <nav class="nav">
      ${nav.map(n=>`<a href="${n.href}" class="nav-item${active===n.id?' active':''}"><span class="nav-icon">${n.icon}</span>${n.label}</a>`).join('')}
    </nav>
    <div class="nav-divider"></div>
    <div class="sidebar-user">
      <div style="display:flex;align-items:center;gap:9px;margin-bottom:8px">
        <div class="user-avatar">${initial}</div>
        <div>
          <div class="user-name">${user.name||'User'}</div>
          <div class="user-email">${user.email||''}</div>
        </div>
      </div>
      <button class="logout-btn" onclick="logout()">⏻ Logout</button>
    </div>
  </aside>`;
}

// SPINNER
function spin(id,val){
  const el=document.getElementById(id);
  if(!el)return;
  if(!el.hasOwnProperty('_origHtml')) el._origHtml=el.innerHTML;
  el.disabled=val;
  if(val) el.innerHTML='<span style="display:inline-block;width:14px;height:14px;border:2px solid rgba(255,255,255,.3);border-top-color:#fff;border-radius:50%;animation:spin .7s linear infinite;vertical-align:middle;margin-right:6px"></span> Processing...';
  else el.innerHTML=el._origHtml;
}