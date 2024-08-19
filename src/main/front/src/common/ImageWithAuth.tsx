import React, {useEffect, useState} from 'react';
import axios from "./axios";

type ImageWithAuthProps = {
    url: string;
}

export const ImageWithAuth: React.FC<ImageWithAuthProps> = ({url}) => {

    const [fetchedImage, setFetchedImage] = useState<string | undefined>()
    const img: React.Ref<HTMLImageElement> = React.createRef()

    useEffect(() => {
        axios
            .get(`${url}`, {
                responseType: 'arraybuffer',
                headers: {
                    Accept: 'image/jpeg',
                },
            })
            .then((res) => {
                const blob = new Blob([res.data], {
                    type: 'image/jpeg',
                })

                const objectURL = URL.createObjectURL(blob)
                setFetchedImage(objectURL)
            })
    }, [url])


    useEffect(() => {
        // Make sure img.current is not null and image is fetched
        if (img.current && fetchedImage) {
            img.current.src = fetchedImage
        }
    }, [fetchedImage])

    return <img src={''} alt={'Loading...'} ref={img}/>
}

export default ImageWithAuth