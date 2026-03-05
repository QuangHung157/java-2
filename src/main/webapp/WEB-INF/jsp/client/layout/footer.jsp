<%@page contentType="text/html" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <c:set var="ZALO_PHONE" value="0987654321" />
    <c:set var="HOTLINE_NUMBER" value="02471069999" />
    <c:set var="MESSENGER_LINK" value="https://m.me/khuat.hung.900" />

    <div class="container-fluid bg-dark text-white-50 footer pt-5 mt-5">
      <div class="container py-5">
        <div class="pb-4 mb-4" style="border-bottom: 1px solid rgba(226, 175, 24, 0.5) ;">
          <div class="row g-4">
            <div class="col-lg-3">
              <a href="${pageContext.request.contextPath}/" class="text-decoration-none">
                <h1 class="text-primary mb-0">Laptopshop</h1>
                <p class="text-secondary mb-0">Quanghung</p>
              </a>
            </div>
          </div>
        </div>

        <div class="row g-5">
          <div class="col-lg-3 col-md-6">
            <div class="footer-item">
              <h4 class="text-light mb-3">Quality is our top priority</h4>
              <p class="text-white-50 mb-0">Trusted laptops, professional consulting, reliable service.</p>
            </div>
          </div>

          <div class="col-lg-3 col-md-6">
            <div class="d-flex flex-column text-start footer-item">
              <h4 class="text-light mb-3">Shop Info</h4>
              <a class="btn-link" href="${pageContext.request.contextPath}/about">About Us</a>
              <a class="btn-link" href="${pageContext.request.contextPath}/products">Products</a>
              <a class="btn-link" href="${pageContext.request.contextPath}/policies">Policies</a>
            </div>
          </div>

          <div class="col-lg-3 col-md-6">
            <div class="d-flex flex-column text-start footer-item">
              <h4 class="text-light mb-3">Account</h4>
              <c:choose>
                <c:when test="${pageContext.request.userPrincipal != null}">
                  <a class="btn-link" href="${pageContext.request.contextPath}/account">My Account</a>
                  <a class="btn-link" href="${pageContext.request.contextPath}/orders">My Orders</a>
                  <a class="btn-link" href="${pageContext.request.contextPath}/logout">Logout</a>
                </c:when>
                <c:otherwise>
                  <a class="btn-link" href="${pageContext.request.contextPath}/login">Login</a>
                  <a class="btn-link" href="${pageContext.request.contextPath}/register">Register</a>
                </c:otherwise>
              </c:choose>
            </div>
          </div>

          <div class="col-lg-3 col-md-6">
            <div class="footer-item">
              <h4 class="text-light mb-3">Contact</h4>
              <p class="mb-1">Author: QuangHung</p>
              <p class="mb-1">Email: Quanghung@gmail.com</p>
              <p class="mb-3">
                Phone:
                <a href="tel:${HOTLINE_NUMBER}" class="text-white-50 text-decoration-none">${HOTLINE_NUMBER}</a>
              </p>

              <div class="footer-social">
                <a class="footer-social-btn zalo" target="_blank" rel="noopener" href="https://zalo.me/${ZALO_PHONE}">
                  <i class="fa-solid fa-comment-dots"></i>
                  <span>Zalo Chat</span>
                </a>
                <a class="footer-social-btn mess" target="_blank" rel="noopener" href="${MESSENGER_LINK}">
                  <i class="fa-brands fa-facebook-messenger"></i>
                  <span>Messenger</span>
                </a>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>

    <div class="container-fluid copyright bg-dark py-4">
      <div class="container">
        <div class="row">
          <div class="col-md-6 text-center text-md-start mb-3 mb-md-0">
            <span class="text-light">
              <a href="${pageContext.request.contextPath}/" class="text-light text-decoration-none">
                <i class="fas fa-copyright text-light me-2"></i>QuangHung
              </a>, All rights reserved.
            </span>
          </div>
          <div class="col-md-6 my-auto text-center text-md-end text-white-50">
            Spring Boot MVC • JSP • JSTL
          </div>
        </div>
      </div>
    </div>

    <div class="quick-contact">
      <a class="qc-item qc-mess" target="_blank" rel="noopener" href="${MESSENGER_LINK}">
        <i class="fa-brands fa-facebook-messenger"></i>
        <span>Chat Messenger</span>
      </a>
      <a class="qc-item qc-zalo" target="_blank" rel="noopener" href="https://zalo.me/${ZALO_PHONE}">
        <i class="fa-solid fa-comment-dots"></i>
        <span>Chat Zalo</span>
      </a>
      <a class="qc-item qc-call" href="tel:${HOTLINE_NUMBER}">
        <i class="fa-solid fa-phone"></i>
        <span>${HOTLINE_NUMBER}</span>
      </a>
    </div>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    <style>
      .footer-social {
        display: flex;
        gap: 10px;
        flex-wrap: wrap
      }

      .footer-social-btn {
        display: flex;
        align-items: center;
        gap: 8px;
        padding: 8px 12px;
        border-radius: 999px;
        color: #fff;
        text-decoration: none;
        font-size: 14px;
        font-weight: 600;
        box-shadow: 0 6px 18px rgba(0, 0, 0, .3)
      }

      .footer-social-btn.zalo {
        background: #0068ff
      }

      .footer-social-btn.mess {
        background: #0084ff
      }

      .quick-contact {
        position: fixed;
        left: 18px;
        bottom: 80px;
        z-index: 99998;
        display: flex;
        flex-direction: column;
        gap: 10px
      }

      .qc-item {
        display: flex;
        align-items: center;
        gap: 10px;
        background: #fff;
        color: #111;
        text-decoration: none;
        padding: 10px 12px;
        border-radius: 14px;
        box-shadow: 0 8px 22px rgba(0, 0, 0, .2)
      }

      .qc-item i {
        width: 34px;
        height: 34px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff
      }

      .qc-mess i {
        background: #0084ff
      }

      .qc-zalo i {
        background: #0068ff
      }

      .qc-call i {
        background: #ff7a00
      }

      @media(max-width:576px) {
        .qc-item span {
          display: none
        }
      }
    </style>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    <style>
      .ai-chat-btn {
        position: fixed;
        bottom: 30px;
        right: 30px;
        width: 60px;
        height: 60px;
        background-color: #198754;
        color: #fff;
        border-radius: 50%;
        border: none;
        cursor: pointer;
        z-index: 99999;
        box-shadow: 0 6px 18px rgba(0, 0, 0, 0.35);
        transition: transform 0.2s ease;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 26px;
      }

      .ai-chat-btn:hover {
        transform: scale(1.06);
        background-color: #157347;
      }

      .ai-chat-window {
        position: fixed;
        bottom: 100px;
        right: 30px;
        width: 360px;
        height: 500px;
        max-width: calc(100vw - 60px);
        background: #fff;
        border-radius: 14px;
        z-index: 99999;
        box-shadow: 0 10px 35px rgba(0, 0, 0, .25);
        border: 1px solid #e6e6e6;

        display: flex;
        flex-direction: column;
        overflow: hidden;

        opacity: 0;
        visibility: hidden;
        transform: translateY(18px);
        transition: all .25s ease;
      }

      .ai-chat-window.active {
        opacity: 1;
        visibility: visible;
        transform: translateY(0);
      }

      .ai-chat-header {
        background: linear-gradient(135deg, #198754, #20c997);
        color: #fff;
        padding: 12px 14px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-weight: 700;
      }

      .ai-chat-body {
        flex: 1;
        padding: 14px;
        overflow-y: auto;
        background: #f6f7f9;
        display: flex;
        flex-direction: column;
        gap: 12px;
      }

      .ai-msg {
        max-width: 85%;
        padding: 10px 12px;
        border-radius: 12px;
        font-size: 14px;
        line-height: 1.45;
        word-break: break-word;
      }

      .ai-msg.bot {
        align-self: flex-start;
        background: #fff;
        border: 1px solid #e6e6e6;
        border-bottom-left-radius: 3px;
        color: #333;
      }

      .ai-msg.user {
        align-self: flex-end;
        background: #d1e7dd;
        color: #0f5132;
        border-bottom-right-radius: 3px;
      }

      .ai-chat-footer {
        padding: 12px;
        border-top: 1px solid #eee;
        background: #fff;
        display: flex;
        gap: 8px;
      }

      .ai-chat-input {
        flex: 1;
        border: 1px solid #ced4da;
        border-radius: 18px;
        padding: 8px 12px;
        outline: none;
      }

      .ai-chat-send {
        width: 42px;
        height: 42px;
        border-radius: 50%;
        border: none;
        cursor: pointer;
        background: #198754;
        color: #fff;
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .ai-chat-send:disabled {
        opacity: .6;
        cursor: not-allowed;
      }

      .ai-typing {
        font-size: 12px;
        color: #888;
        font-style: italic;
        margin: 0 14px 10px;
        display: none;
      }
    </style>

    <button class="ai-chat-btn" type="button" onclick="aiToggleChat()" title="Tư vấn AI">
      <i class="fas fa-comment-dots"></i>
    </button>

    <div class="ai-chat-window" id="aiChatWindow">
      <div class="ai-chat-header">
        <span><i class="fas fa-robot me-2"></i> Trợ lý LaptopShop</span>
        <button type="button" class="btn-close btn-close-white" onclick="aiToggleChat()"></button>
      </div>

      <div class="ai-chat-body" id="aiChatBody">
        <div class="ai-msg bot">
          Xin chào! 👋<br>
          Mình là AI tư vấn của LaptopShop.<br>
          Bạn có thể hỏi: “Acer có mấy máy?”, “Máy rẻ nhất?”,”.
        </div>
      </div>

      <div class="ai-typing" id="aiTyping">AI đang trả lời...</div>

      <div class="ai-chat-footer">
        <input id="aiChatInput" class="ai-chat-input" type="text" placeholder="Nhập tin nhắn..."
          onkeypress="aiHandleEnter(event)">
        <button id="aiChatSendBtn" class="ai-chat-send" type="button" onclick="aiSendMessage()">
          <i class="fas fa-paper-plane"></i>
        </button>
      </div>
    </div>

    <script>
      const AI_CSRF_HEADER = "${_csrf.headerName}";
      const AI_CSRF_TOKEN = "${_csrf.token}";
      const AI_BASE_URL = "${pageContext.request.contextPath}";

      let aiPending = false;
      let aiLastSendAt = 0;

      function aiToggleChat() {
        const w = document.getElementById("aiChatWindow");
        w.classList.toggle("active");
        if (w.classList.contains("active")) {
          setTimeout(() => document.getElementById("aiChatInput").focus(), 50);
        }
      }

      function aiHandleEnter(e) {
        if (e.key === "Enter") {
          e.preventDefault();
          aiSendMessage();
        }
      }

      function escapeHtml(str) {
        return (str || "").replace(/[&<>"']/g, function (m) {
          return ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#039;" })[m];
        });
      }

      function aiAppend(text, role) {
        const body = document.getElementById("aiChatBody");
        const div = document.createElement("div");
        div.className = "ai-msg " + role;
        div.innerHTML = escapeHtml((text || "").toString()).replace(/\n/g, "<br>");
        body.appendChild(div);
        body.scrollTop = body.scrollHeight;
      }

      function aiGetSessionId() {
        let sid = localStorage.getItem("laptopshop_chat_session_id");
        if (!sid) {
          if (window.crypto && crypto.randomUUID) sid = crypto.randomUUID();
          else sid = "sid_" + Date.now() + "_" + Math.random().toString(16).slice(2);
          localStorage.setItem("laptopshop_chat_session_id", sid);
        }
        return sid;
      }

      async function aiSendMessage() {
        if (aiPending) return;

        const now = Date.now();
        if (now - aiLastSendAt < 350) return;
        aiLastSendAt = now;

        const input = document.getElementById("aiChatInput");
        const sendBtn = document.getElementById("aiChatSendBtn");
        const typing = document.getElementById("aiTyping");

        const message = (input.value || "").trim();
        if (!message) return;

        aiPending = true;

        aiAppend(message, "user");
        input.value = "";

        typing.style.display = "block";
        sendBtn.disabled = true;

        try {
          const headers = { "Content-Type": "application/json" };
          if (AI_CSRF_HEADER && AI_CSRF_TOKEN) headers[AI_CSRF_HEADER] = AI_CSRF_TOKEN;

          const controller = new AbortController();
          const timeoutId = setTimeout(() => controller.abort(), 25000);

          const payload = { message: message, sessionId: aiGetSessionId() };

          const res = await fetch(AI_BASE_URL + "/api/chat", {
            method: "POST",
            headers,
            body: JSON.stringify(payload),
            signal: controller.signal
          });

          clearTimeout(timeoutId);

          if (res.status === 429) {
            let txt = "Bạn gửi hơi nhanh 😅 Chờ vài giây rồi thử lại nhé.";
            try {
              const data429 = await res.json();
              if (data429 && data429.reply) txt = data429.reply;
            } catch (e) { }
            aiAppend(txt, "bot");
            return;
          }

          if (!res.ok) throw new Error("HTTP " + res.status);

          const data = await res.json();
          const reply = (data && data.reply) ? data.reply : "Mình chưa trả lời được, bạn thử lại nhé.";
          aiAppend(reply, "bot");

        } catch (err) {
          console.error(err);
          aiAppend("Xin lỗi, hệ thống đang bận hoặc lỗi kết nối. Bạn thử lại sau nhé!", "bot");
        } finally {
          typing.style.display = "none";
          sendBtn.disabled = false;
          aiPending = false;
        }
      }
    </script>