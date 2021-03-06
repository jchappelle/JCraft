/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes;

import com.jme3.math.Vector3f;
import java.io.Serializable;

/**
 *
 * @author Carl
 */
public class Vector3Int implements Serializable
{

    public Vector3Int(int x, int y, int z){
        this();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3Int(){

    }
    public int x;
    public int y;
    public int z;

    public int getX(){
        return x;
    }

    public Vector3Int setX(int x){
        this.x = x;
        return this;
    }

    public int getY(){
        return y;
    }

    public Vector3Int setY(int y){
        this.y = y;
        return this;
    }

    public int getZ(){
        return z;
    }

    public Vector3Int setZ(int z){
        this.z = z;
        return this;
    }

    public boolean hasNegativeCoordinate(){
        return ((x < 0) || (y < 0) || (z < 0));
    }

    public Vector3Int set(Vector3Int vector3Int){
        return set(vector3Int.getX(), vector3Int.getY(), vector3Int.getZ());
    }

    public Vector3Int set(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3Int add(Vector3Int vector3Int){
        return add(vector3Int.getX(), vector3Int.getY(), vector3Int.getZ());
    }

    public Vector3Int add(int x, int y, int z){
        return new Vector3Int(this.x + x, this.y + y, this.z + z);
    }

    public Vector3Int addLocal(Vector3Int vector3Int){
       return addLocal(vector3Int.getX(), vector3Int.getY(), vector3Int.getZ());
    }

    public Vector3Int addLocal(int x, int y, int z){
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3Int subtract(Vector3Int vector3Int){
        return subtract(vector3Int.getX(), vector3Int.getY(), vector3Int.getZ());
    }

    public Vector3Int subtract(int x, int y, int z){
        return new Vector3Int(this.x - x, this.y - y, this.z - z);
    }

    public Vector3Int subtractLocal(Vector3Int vector3Int){
        return subtractLocal(vector3Int.getX(), vector3Int.getY(), vector3Int.getZ());
    }

    public Vector3Int subtractLocal(int x, int y, int z){
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vector3Int negate(){
        return mult(-1);
    }

    public Vector3Int mult(int factor){
        return mult(factor, factor, factor);
    }

    public Vector3Int mult(int x, int y, int z){
        return new Vector3Int(this.x * x, this.y * y, this.z * z);
    }

    public Vector3Int mult(Vector3Int factor)
    {
        return mult(factor.x, factor.y, factor.z);
    }

    public Vector3Int multLocal(Vector3Int factor)
    {
        return multLocal(factor.x, factor.y, factor.z);
    }

    public Vector3Int divide(int factor)
    {
        return fromVector3f(toVector3f().divide(factor));
    }

    public Vector3Int divide(Vector3Int factor)
    {
        return fromVector3f(toVector3f().divide(factor.toVector3f()));
    }

    public Vector3Int divide(int x, int y, int z)
    {
        return fromVector3f(toVector3f().divide(new Vector3f(x, y, z)));
    }

    public Vector3Int negateLocal(){
        return multLocal(-1);
    }

    public Vector3Int multLocal(int factor){
        return multLocal(factor, factor, factor);
    }

    public Vector3Int multLocal(int x, int y, int z){
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public Vector3f toVector3f()
    {
        return new Vector3f(x, y, z);
    }

    public static Vector3Int fromVector3f(Vector3f vector)
    {
        vector = Util.compensateFloatRoundingErrors(vector);
        return new Vector3Int((int)vector.x, (int)vector.y, (int)vector.z);
    }

    @Override
    public Vector3Int clone(){
        return new Vector3Int(x, y, z);
    }

@Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        Vector3Int other = (Vector3Int) obj;
        if(x != other.x)
            return false;
        if(y != other.y)
            return false;
        if(z != other.z)
            return false;
        return true;
    }
    @Override
    public String toString(){
        return "[" + x + ", " + y + ", " + z + "]";
    }
}
