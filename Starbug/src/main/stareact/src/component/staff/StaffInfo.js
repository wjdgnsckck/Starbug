//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ mui table ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//
import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
// 테이블 페이징 처리
import Pagination from '@mui/material/Pagination';
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ mui table ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//

import { BrowserRouter , Routes , Route , Link , useSearchParams , useParams } from 'react-router-dom';


import axios from 'axios';
import {useState , useEffect} from 'react';

import Address from './Address';

import style from './StaffInfo.css';
import StaffRegist from './StaffRegist';
import StaffUpdate from './StaffUpdate';

export default function StaffInfo(props){

    // 직원 정보 호출
    const [ slist , setSlist ] = useState( { staffListDtos : [] , totalPage : 0 , tcotalCount : 0 }  );
    //console.log(slist)

    // 페이징
    const [pageInfo , setPageInfo ] = useState( { page : 1 , keyword : ''} ); // page : 페이지수 , keyword : 검색어(이름)

    useEffect( ()=>{ getStaffList(); } , [pageInfo.page])//useEffect // pageInfo 값이 변경될때 axios 실행

    // 공용 axios
    const getStaffList = (e) =>{
         axios
                .get("/staff/staffGet" , {params : pageInfo}) // pageInfo 객체 전송
                .then(r=> {//console.log(r.data)
                    setSlist(r.data); // 응답 받은 모든 게시물 저장
                })//then
    }



    // 페이지 번호 클릭
    const onPageSelect = ( e , value )=> {console.log('value 는 무엇인가 : ');console.log(value);console.log('e는 무엇인가 : ');console.log(e);
        pageInfo.page=value; // 클릭한 페이지번호로 pageInfo 를 변경 해준다[기본값은 1]
        setPageInfo({...pageInfo});  // ...~!@~!@  -> 새로고침 [ 상태 변수의 주소값이 바뀌면 재랜더링 ]
    }

    // 3. 검색 버튼 클릭
    const onSearch= (e) => {console.log('검색버튼 클릮했을때 e : '); console.log(e);
        setPageInfo({...pageInfo , page : 1})
        getStaffList();
    }

    // sno update로 전달
    const [ item , setItem ] = useState({sno : 'sno'});




return(<>

    <div className="iwrap">
        <div className="istaffWrap">
        <TableContainer component={Paper}>
          <Table sx={{ minWidth: 650 }} aria-label="simple table">
          {/*테이블 헤더*/}
            <TableHead>
              <TableRow>
                <TableCell align="center">이름</TableCell>
                <TableCell align="center">직급</TableCell>
                <TableCell align="center">성별</TableCell>
                <TableCell align="center">연락처</TableCell>
                <TableCell align="center">이메일</TableCell>
                <TableCell align="center">입사일</TableCell>
                <TableCell align="center">상세</TableCell>
              </TableRow>
            </TableHead>
             {/*테이블 헤더*/}

             {/*테이블 내용*/}
            <TableBody>
              {slist.staffListDtos.map((row) => ( // map is not a function
                  <TableRow
                    key={row.sname}
                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }} >
                  <TableCell style={{ width : '15%'}} align="center" >{row.sname}</TableCell>
                  <TableCell style={{ width : '10%'}}align="center">{row.sposition}</TableCell>
                  <TableCell style={{ width : '10%'}}align="center">{row.ssex}</TableCell>
                  <TableCell style={{ width : '18%'}}align="center">{row.sphone}</TableCell>
                  <TableCell style={{ width : '20%'}}align="center">{row.semail}</TableCell>
                  <TableCell style={{ width : '15%'}}align="center">{row.sjoined}</TableCell>
                  <TableCell style={{ width : '12%'}}align="center">
                  <Button style={ {color :'#9A8C81' , borderColor : '#9A8C81'} } size="small" variant="outlined" onClick={()=>{setItem(row.sno)}}>조회</Button>
                  </TableCell>
                   </TableRow>
              ))}
            </TableBody>
             {/*테이블 내용*/}
          </Table>
        </TableContainer>
            <div style={{ display: 'flex' , justifyContent : 'center' , margin : '10px 10px 30px 10px' , alignItems: 'center' }}>
            <Pagination page={pageInfo.page} count={slist.totalPage} onChange={onPageSelect} size="large"/>
                <input className="keyword" type="text" value={pageInfo.keyword} placeholder="이름을 검색 하세요" onChange={ (e) => {setPageInfo({...pageInfo , keyword : e.target.value}) } } />
                <button className="sbtnCss" type="button" onClick={onSearch}> 검색 </button>
            </div>

        <StaffUpdate key={item} item ={item}   />

        </div> {/*staffWrap*/}
    </div> {/*swrap*/}

</>)
}










/*

    <div className="infoCss">
        <table>
            <tr>
                <th>이름</th><th>직급</th><th>성별</th><th>연락처</th><th>이메일</th><th>입사일</th>
            </tr>

            {
            slist.map(row=>{
               return(<>
                <tr>
                    <td>{row.sname}</td><td>{row.sposition}</td><td>{row.ssex}</td>
                    <td>{row.sphone}</td><td>{row.semail}</td><td>{row.sjoined}</td>
                </tr>
                </>)
                })
            }
        </table>
    </div>



            <TableBody>
              {slist.staffListDtos.map((row) => ( // map is not a function
                  <TableRow
                    key={row.sname}
                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }} >
                  <TableCell align="center">{row.sname}</TableCell>
                  <TableCell align="center">{row.sposition}</TableCell>
                  <TableCell align="center">{row.ssex}</TableCell>
                  <TableCell align="center">{row.sphone}</TableCell>
                  <TableCell align="center">{row.semail}</TableCell>
                  <TableCell align="center">{row.sjoined}</TableCell>
                </TableRow>
              ))}
            </TableBody>



                    <Link to={"/staff/StaffUpdate?sno="+row.sno}>
                        {row.sname}
                    </Link>


onClick={setItem(row)}  얘가 문제 였음

*/