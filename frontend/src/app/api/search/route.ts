import { NextRequest, NextResponse } from "next/server";
import { SERVICES } from "@/config/services";

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();

    const backendResponse = await fetch(`${SERVICES.API_GATEWAY}/api/search`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    });

    const text = await backendResponse.text();
    let data: unknown = text;

    try {
      data = JSON.parse(text);
    } catch {
      // 응답이 JSON이 아니면 그대로 문자열 반환
    }

    if (!backendResponse.ok) {
      return NextResponse.json(
        {
          message:
            typeof data === "object" && data !== null && "message" in data
              ? (data as { message: string }).message
              : typeof data === "string"
                ? data
                : "검색 요청이 실패했습니다.",
          status: backendResponse.status,
        },
        { status: backendResponse.status },
      );
    }

    return new NextResponse(
      typeof data === "string" ? data : JSON.stringify(data),
      {
        status: backendResponse.status,
        headers: {
          "Content-Type":
            typeof data === "string"
              ? backendResponse.headers.get("content-type") ?? "text/plain"
              : "application/json",
        },
      },
    );
  } catch (error) {
    console.error("[API/search] 프록시 요청 실패:", error);

    return NextResponse.json(
      { message: "백엔드 검색 서비스와 통신하지 못했습니다." },
      { status: 500 },
    );
  }
}

