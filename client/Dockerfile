# ============================================================
# STAGE 1: BUILD — 用 Node 編譯 Angular 為靜態檔案
# ============================================================
FROM node:20-alpine AS builder
WORKDIR /app

# 先複製套件清單並安裝依賴，利用 layer 快取（清單沒變就不重裝）。
# npm ci 依 package-lock.json 做乾淨、可重現的安裝。
COPY package.json package-lock.json ./
RUN npm ci

# 複製其餘原始碼並以 production 模式建置。
COPY . .
RUN npm run build -- --configuration production

# ============================================================
# STAGE 2: RUNTIME — 用 nginx 提供靜態檔案
# ============================================================
FROM nginx:alpine AS runtime

# 移除預設設定，換成我們為 Angular SPA 調整過的設定。
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Angular 17 的 application builder 會輸出到 dist/client/browser/
COPY --from=builder /app/dist/client/browser /usr/share/nginx/html

EXPOSE 80
# nginx 需以前景執行，容器才不會立即結束。
CMD ["nginx", "-g", "daemon off;"]
