"use client";

import axios from "axios";
import { useStore } from "@/store";

export default function Home() {
  const searchInput = useStore((state) => state.searchInput);
  const setSearchInput = useStore((state) => state.setSearchInput);

  const testAlert = async () => {
    const message = searchInput.trim();

    if (!message) {
      window.alert("검색어를 입력해주세요.");
      return;
    }

    try {
      // Next.js API Routes를 통해 호출 (CORS 문제 해결)
      const response = await axios.post("/api/search", {
        domain: "default",
        keyword: message,
      });

      console.log("API 응답:", response.data);
      const resultMessage =
        typeof response.data === "object" && response.data !== null
          ? (response.data as { message?: string }).message
          : undefined;

      window.alert(resultMessage ?? "검색 요청이 전송되었습니다.");
    } catch (error) {
      console.error("검색 요청 실패:", error);

      if (axios.isAxiosError(error)) {
        const message =
          error.response?.data?.message || error.message || "알 수 없는 오류";
        window.alert(`요청 중 오류가 발생했습니다.\n${message}`);
        return;
      }

      window.alert("요청 중 예기치 못한 오류가 발생했습니다.");
    }
  };


  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-50 font-sans">
      <div className="flex w-full max-w-3xl flex-col items-center gap-8 px-4">
        {/* 질문 텍스트 */}
        <h1 className="text-center text-2xl font-normal text-gray-900">
          한국 축구 K리그 관련 질문만 받습니다. 다른 분야는 대답합니다
        </h1>

        {/* 입력 필드 */}
        <div className="relative w-full">
          <div className="flex items-center rounded-full border border-gray-300 bg-white px-4 py-3 shadow-lg transition-all hover:border-gray-400 focus-within:border-gray-400">
            {/* 왼쪽 + 아이콘 */}
            <button
              type="button"
              className="mr-3 flex h-6 w-6 items-center justify-center text-gray-700 hover:text-gray-900"
            >
              <svg
                width="16"
                height="16"
                viewBox="0 0 16 16"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  d="M8 3V13M3 8H13"
                  stroke="currentColor"
                  strokeWidth="1.5"
                  strokeLinecap="round"
                />
              </svg>
            </button>

            {/* 입력 필드 */}
            <input
              type="text"
              value={searchInput}
              onChange={(e) => setSearchInput(e.target.value)}
              placeholder="Ask for the latest game score"
              className="flex-1 bg-transparent text-gray-900 placeholder:text-gray-500 focus:outline-none"
            />

            {/* 오른쪽 아이콘들 */}
            <div className="ml-3 flex items-center gap-2">
              {/* 마이크 아이콘 */}
              <button
                type="button"
                onClick={testAlert}
                className="flex h-8 w-8 items-center justify-center text-gray-700 hover:text-gray-900"
              >
                <svg
                  width="16"
                  height="16"
                  viewBox="0 0 16 16"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    d="M8 1C7.17157 1 6.5 1.67157 6.5 2.5V8C6.5 8.82843 7.17157 9.5 8 9.5C8.82843 9.5 9.5 8.82843 9.5 8V2.5C9.5 1.67157 8.82843 1 8 1Z"
                    stroke="currentColor"
                    strokeWidth="1.5"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                  <path
                    d="M4 7V8C4 10.2091 5.79086 12 8 12C10.2091 12 12 10.2091 12 8V7"
                    stroke="currentColor"
                    strokeWidth="1.5"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                  <path
                    d="M8 12V15M5 15H11"
                    stroke="currentColor"
                    strokeWidth="1.5"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                </svg>
              </button>

              {/* 오디오 파형 아이콘 */}
              <button
                type="button"
                className="flex h-8 w-8 items-center justify-center rounded-full bg-gray-200 text-gray-700 hover:bg-gray-300"
              >
                <svg
                  width="12"
                  height="12"
                  viewBox="0 0 12 12"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <rect x="1" y="4" width="1.5" height="4" fill="currentColor" />
                  <rect x="4" y="2" width="1.5" height="8" fill="currentColor" />
                  <rect x="7" y="3" width="1.5" height="6" fill="currentColor" />
                  <rect x="10" y="1" width="1.5" height="10" fill="currentColor" />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
