import axios from 'axios';

import { useState, useEffect } from 'react';

import Header from '../Header.js';
import styles from './Parcel.css';

import ParcelOrder from './ParcelOrder';
import ParcelView from './ParcelView';
import InventoryLog from './InventoryLog';
import { Link } from 'react-router-dom';

export default function Parcel( props ){

    let [ parcelState , setParcelState ] = useState(1);

    const selectParcel = (num) =>{
        setParcelState( num );
    }

    return(<>
        <div>
            <div className="parcelCategoryBox" >
                <div
                className={`parcelCategory ${parcelState ==1 ? 'selectParcelCategory' : ''}`}
                onClick={ () => selectParcel(1) }>
                    주문하기
                </div>
                <div
                className={`parcelCategory ${parcelState ==2 ? 'selectParcelCategory' : ''}`}
                onClick={ () => selectParcel(2) }>
                    발주확인
                </div>
                <div
                className={`parcelCategory ${parcelState ==3 ? 'selectParcelCategory' : ''}`}
                onClick={ () => selectParcel(3) }>
                    재고확인
                </div>
            </div>
            <div className="parcelComponent">
            { parcelState === 1 && <ParcelOrder /> }
            { parcelState === 2 && <ParcelView /> }
            { parcelState === 3 && <InventoryLog /> }
            </div>
        </div>
    </>)
}