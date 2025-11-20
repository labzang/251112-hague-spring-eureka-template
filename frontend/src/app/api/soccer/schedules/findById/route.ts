import { NextRequest, NextResponse } from 'next/server';
import { SERVICES, SOCCER_ENDPOINTS } from '@/config/services';

/**
 * Soccer Service - Schedules findById API Route
 */
export async function POST(request: NextRequest) {
  try {
    const body = await request.json();
    const apiGatewayUrl = SERVICES.API_GATEWAY;
    const endpoint = `${SOCCER_ENDPOINTS.SCHEDULES}/findById`;

    const response = await fetch(`${apiGatewayUrl}${endpoint}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body),
    });

    const data = await response.json();

    if (!response.ok) {
      return NextResponse.json(
        {
          message: data.message || '스케줄 조회에 실패했습니다.',
          status: response.status,
        },
        { status: response.status }
      );
    }

    return NextResponse.json(data);
  } catch (error) {
    console.error('[API/soccer/schedules/findById] POST 요청 실패:', error);
    return NextResponse.json(
      { message: '스케줄 조회 중 오류가 발생했습니다.' },
      { status: 500 }
    );
  }
}

