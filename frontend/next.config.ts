import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  output: 'standalone',
  // src 디렉토리를 사용하도록 설정
  // Next.js는 기본적으로 src/app을 자동으로 인식합니다
};

export default nextConfig;
