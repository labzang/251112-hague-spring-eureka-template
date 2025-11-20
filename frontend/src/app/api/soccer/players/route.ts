import { NextRequest, NextResponse } from 'next/server';
import { SERVICES, SOCCER_ENDPOINTS } from '@/config/services';

/**
 * Soccer Service - Players API Routes
 */
export async function GET(request: NextRequest) {
  try {
    const apiGatewayUrl = SERVICES.API_GATEWAY;
    const endpoint = SOCCER_ENDPOINTS.PLAYERS;

    const response = await fetch(`${apiGatewayUrl}${endpoint}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
      cache: 'no-store',
    });

    const data = await response.json();

    if (!response.ok) {
      return NextResponse.json(
        {
          message: data.message || '선수 조회에 실패했습니다.',
          status: response.status,
        },
        { status: response.status }
      );
    }

    return NextResponse.json(data);
  } catch (error) {
    console.error('[API/soccer/players] GET 요청 실패:', error);
    return NextResponse.json(
      { message: '선수 조회 중 오류가 발생했습니다.' },
      { status: 500 }
    );
  }
}

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();
    const apiGatewayUrl = SERVICES.API_GATEWAY;
    const endpoint = SOCCER_ENDPOINTS.PLAYERS;

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
          message: data.message || '선수 저장에 실패했습니다.',
          status: response.status,
        },
        { status: response.status }
      );
    }

    return NextResponse.json(data);
  } catch (error) {
    console.error('[API/soccer/players] POST 요청 실패:', error);
    return NextResponse.json(
      { message: '선수 저장 중 오류가 발생했습니다.' },
      { status: 500 }
    );
  }
}

export async function PUT(request: NextRequest) {
  try {
    const body = await request.json();
    const apiGatewayUrl = SERVICES.API_GATEWAY;
    const endpoint = SOCCER_ENDPOINTS.PLAYERS;

    const response = await fetch(`${apiGatewayUrl}${endpoint}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body),
    });

    const data = await response.json();

    if (!response.ok) {
      return NextResponse.json(
        {
          message: data.message || '선수 수정에 실패했습니다.',
          status: response.status,
        },
        { status: response.status }
      );
    }

    return NextResponse.json(data);
  } catch (error) {
    console.error('[API/soccer/players] PUT 요청 실패:', error);
    return NextResponse.json(
      { message: '선수 수정 중 오류가 발생했습니다.' },
      { status: 500 }
    );
  }
}

export async function DELETE(request: NextRequest) {
  try {
    const body = await request.json();
    const apiGatewayUrl = SERVICES.API_GATEWAY;
    const endpoint = SOCCER_ENDPOINTS.PLAYERS;

    const response = await fetch(`${apiGatewayUrl}${endpoint}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body),
    });

    const data = await response.json();

    if (!response.ok) {
      return NextResponse.json(
        {
          message: data.message || '선수 삭제에 실패했습니다.',
          status: response.status,
        },
        { status: response.status }
      );
    }

    return NextResponse.json(data);
  } catch (error) {
    console.error('[API/soccer/players] DELETE 요청 실패:', error);
    return NextResponse.json(
      { message: '선수 삭제 중 오류가 발생했습니다.' },
      { status: 500 }
    );
  }
}

